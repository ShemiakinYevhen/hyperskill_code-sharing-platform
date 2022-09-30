<!DOCTYPE html>
<html>
<head>
    <title>Latest</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Montserrat">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <style>
        body, h1,h2,h3,h4,h5,h6 {font-family: "Montserrat", sans-serif}
        .w3-row-padding img {margin-bottom: 12px}
        /* Set the width of the sidebar to 120px */
        .w3-sidebar {width: 120px;background: #222;}
        /* Add a left margin to the "page content" that matches the width of the sidebar (120px) */
        #main {margin-left: 120px}
        /* Remove margins from "page content" on small screens */
        @media only screen and (max-width: 600px) {#main {margin-left: 0}}
        .customButton {background-color: #fff;color: #000000;border-radius: 20px;border: 1px solid transparent;font-weight: bold;}
    </style>
</head>
<body class="w3-black">

<!-- Icon Bar (Sidebar - hidden on small screens) -->
<nav class="w3-sidebar w3-bar-block w3-small w3-hide-small w3-center">
    <!-- Avatar image in top left corner -->
    <a href="/" class="w3-bar-item w3-button w3-padding-large w3-hover-black">
        <i class="fa fa-home w3-xxlarge"></i>
        <p>HOME</p>
    </a>
    <a href="/code/new" class="w3-bar-item w3-button w3-padding-large w3-hover-black">
        <i class="fa fa-plus w3-xxlarge"></i>
        <p>ADD</p>
    </a>
    <a href="/code" class="w3-bar-item w3-button w3-padding-large w3-hover-black">
        <i class="fa fa-search w3-xxlarge"></i>
        <p>SEARCH</p>
    </a>
    <a href="/code/latest" class="w3-bar-item w3-button w3-padding-large w3-black">
        <i class="fa fa-history w3-xxlarge"></i>
        <p>LATEST</p>
    </a>
</nav>

<!-- Navbar on small screens (Hidden on medium and large screens) -->
<div class="w3-top w3-hide-large w3-hide-medium" id="myNavbar">
    <div class="w3-bar w3-black w3-opacity w3-hover-opacity-off w3-center w3-small">
        <a href="#" class="w3-bar-item w3-button" style="width:25% !important">HOME</a>
        <a href="#about" class="w3-bar-item w3-button" style="width:25% !important">ADD</a>
        <a href="#photos" class="w3-bar-item w3-button" style="width:25% !important">SEARCH</a>
        <a href="#contact" class="w3-bar-item w3-button" style="width:25% !important">LATEST</a>
    </div>
</div>

<!-- Page Content -->
<div class="w3-padding-large" id="main">
    <!-- Header/Home -->
    <header class="w3-container w3-padding-32 w3-center w3-black" id="home">
        <h1 class="w3-jumbo">Latest records</h1>
    </header>

    <h2>Here are our latest records:</h2>
    <br>
    <#list latestRecords as record>
        <p>Date: <span id="load_date">${record.date}</span></p>
        <br>
        <p>Code:<br><pre id="code_snippet"><code>${record.code}</code></pre></p>
        <br><br>
    </#list>

    <!-- END PAGE CONTENT -->
</div>

</body>
</html>