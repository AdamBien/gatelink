
/*
 |--------------------------------------------------------------------------
 | Browser-sync config file
 |--------------------------------------------------------------------------
 |
 | For up-to-date information about the options:
 |   http://www.browsersync.io/docs/options/
 |
 */
module.exports = {
    "files":"src",
    "server": {
        baseDir: "src",
        routes: {
            "/push": "src"
        }
    },
    "browser": ["google chrome"],
    "startPath": "/push",
    "notify":false
};