package platform.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import platform.business.Record;
import platform.business.RecordService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class RecordController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    RecordService recordService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/code/new")
    public String getFormForNewCodeSnippetCreation() {
        return "uploadNew";
    }

    @PostMapping("/api/code/new")
    public ResponseEntity<String> setNewPieceOfCode(@RequestBody JsonNode body) {
        long timeRestrictions = body.get("time").asLong();
        long viewsRestrictions = body.get("views").asLong();

        Record newRecord = new Record(body.get("code").asText(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                timeRestrictions < 0 ? 0 : timeRestrictions,
                viewsRestrictions < 0 ? 0 : viewsRestrictions);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBodyObject = mapper.createObjectNode();
        newRecord = recordService.save(newRecord);
        responseBodyObject.put("id", newRecord.getUUIDCode());

        try {
            return ResponseEntity.ok().headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Error occurred during json processing!");
        }
    }

    @GetMapping("/code")
    public String getWebPageForSearch() {
        return "search";
    }

    @GetMapping("/code/{recordUUID}")
    public String getWebPageWithPieceOfCode(@PathVariable(value="recordUUID") String recordUUID, Model model) {
        if (recordService.existsByUUIDCode(recordUUID)) {
            Record recordToReturn = recordService.findRecordByUUIDCode(recordUUID);

            if(!checkRestrictions(recordToReturn)) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                updateRestrictions(recordToReturn);
                return ResponseEntity.status(404).headers(headers).body("Record with requested id was not found!").toString();
            }

            updateRestrictions(recordToReturn);
            recordToReturn.setDate(recordToReturn.getDate().truncatedTo(ChronoUnit.MINUTES));
            model.addAttribute("record", recordToReturn);
            model.addAttribute("id", recordUUID);
            return "specificPiece";
        } else {
            model.addAttribute("id", recordUUID);
            return "errorForSpecificPiece";
        }
    }

    @GetMapping("/api/code/{recordUUID}")
    public ResponseEntity<String> getPieceOfCode(@PathVariable(value="recordUUID") String recordId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (recordService.existsByUUIDCode(recordId)) {
            Record recordToReturn = recordService.findRecordByUUIDCode(recordId);

            if (checkRestrictions(recordToReturn)) {
                updateRestrictions(recordToReturn);

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode responseBodyObject = mapper.createObjectNode();
                responseBodyObject.put("code", recordToReturn.getCode());
                responseBodyObject.put("date", recordToReturn.getDate().format(FORMATTER).replace(' ', 'T'));
                responseBodyObject.put("time", recordToReturn.getTime());
                responseBodyObject.put("views", recordToReturn.getViews());

                try {
                    return ResponseEntity.ok().headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
                } catch (JsonProcessingException e) {
                    return ResponseEntity.internalServerError().body("Error occurred during json processing!");
                }
            }
        }
        return ResponseEntity.status(404).headers(headers).body("Record with requested id was not found!");
    }

    @GetMapping("/code/latest")
    public String getWebPageWithPieceOfCode(Model model) {
        ArrayList<Record> listOfLatestRecords = (ArrayList<Record>) StreamSupport.stream(recordService.findAll().spliterator(), false)
                .sorted(Comparator.comparingLong(Record::getId).reversed())
                .filter(RecordController::checkRestrictions)
                .limit(10)
                .collect(Collectors.toList());

        listOfLatestRecords.forEach(record -> record.setDate(record.getDate().truncatedTo(ChronoUnit.MINUTES)));

        if (listOfLatestRecords.isEmpty()) {
            return "errorForNoRecordsYet";
        } else {
            model.addAttribute("latestRecords", listOfLatestRecords);
            return "latestTen";
        }
    }

    @GetMapping("/api/code/latest")
    public ResponseEntity<String> getLatestPiecesOfCode() {
        ArrayList<Record> listOfLatestRecords = (ArrayList<Record>) StreamSupport.stream(recordService.findAll().spliterator(), false)
                .filter(RecordController::checkRestrictions)
                .sorted(Comparator.comparingLong(Record::getId).reversed())
                .limit(10)
                .collect(Collectors.toList());

        if (listOfLatestRecords.isEmpty()) {
            return ResponseEntity.ok().body("No records yet");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode root = mapper.createArrayNode();
            for (Record record : listOfLatestRecords) {
                ObjectNode element = mapper.createObjectNode();
                element.put("code", record.getCode());
                element.put("date", record.getDate().format(FORMATTER).replace(' ', 'T'));
                element.put("time", 0);
                element.put("views", 0);
                root.add(element);
            }
            return ResponseEntity.ok().headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Error occurred during json processing!");
        }
    }

    private static boolean checkRestrictions(Record record) {
        return (record.getTime() == 0 ||
                (record.getTime() > 0 &&
                        Duration.between(record.getDate(), LocalDateTime.now()).toSeconds() < record.getTime())) &&
                record.getViews() >= 0;
    }

    private void updateRestrictions(Record record) {
        if (record.getTime() != 0 && record.getTime() != -1 && record.getTime() - Duration.between(record.getDate(), LocalDateTime.now()).toSeconds() <= 0) {
            record.setTime(-1);
        }

        long viewsValueToDisplay = 0;

        if (record.getViews() == 1) {
            record.setViews(-1);
        } else if (record.getViews() > 1) {
            record.setViews(record.getViews() - 1);
            viewsValueToDisplay = record.getViews();
        } else if (record.getViews() == -1){
            viewsValueToDisplay = -1;
        }

        recordService.save(record);

        if (record.getTime() != 0 && record.getTime() - Duration.between(record.getDate(), LocalDateTime.now()).toSeconds() > 0) {
            record.setTime(record.getTime() - Duration.between(record.getDate(), LocalDateTime.now()).toSeconds());
        }

        record.setViews(viewsValueToDisplay);
    }
}
