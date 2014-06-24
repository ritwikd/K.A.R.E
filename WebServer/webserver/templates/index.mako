<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>K.A.R.E</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/index.css" rel="stylesheet">
    <link href="css/typeaheadjs.css" rel="stylesheet">
    <!--<link href="css/flat-ui.css" rel="stylesheet">-->
</head>

<body>

<div class="wrap container">
    <div class="row">
        <div class="col-md-12 text-center">
            <h1 style="font-size: 64px; font-weight: normal; padding-top: 10%;">K.A.R.E</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 text-center">
            <form id="results-form" role="form">
                <div class="form-group">
                    <input type="text" id="search-box" name="search" class="search-bar form-control"/>
                    <input style="visibility: hidden; max-width: 0; width: 0" type="submit" />
                    <label for="search"  class="sr-only">Search</label>
                </div>
                <div class="form-group">
                    <label for="submit" class="sr-only">Submit</label>
                    <button id="submit" class="btn btn-primary btn-lg">Search</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="footer">
    <div style="text-align: center" class="container">
        <a id = "abtlink" href="about">ABOUT</a>
        <a style="padding-left: 10%" href="https://github.com/arshsab/K.A.R.E"><i class="fa fa-github"></i></a>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/showdown/0.3.1/showdown.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.10.2/bloodhound.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.10.2/typeahead.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.10.2/typeahead.jquery.min.js"></script>
<script src="js/search-bar.js"></script>

</body>
</html>