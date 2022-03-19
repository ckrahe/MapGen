SET maplibCP=maplib\target\maplib-1.1.0.jar;maplib\target\dependency\*;
SET mapsimCP=mapsim\target\mapsim-1.1.0.jar;mapsim\target\dependency\*;
SET maptoolCP=maptool\target\maptool-1.1.0.jar;maptool\target\dependency\*
SET CP=%maplibCP%%mapsimCP%%maptoolCP%

java -cp "%CP%" ^
    -Dwkt="GEOMETRYCOLLECTION (POLYGON ((10.25 9.5, 9.75 9.5, 9.5 9.75, 9.5 10.25, 9.75 10.5, 10.25 10.5, 10.5 10.25, 10.5 9.75, 10.25 9.5)),POLYGON ((9.30 9.20, 9.25 9.20, 9.20 9.25, 9.20 9.30, 9.25 9.35, 9.30 9.35, 9.35 9.30, 9.35 9.25, 9.30 9.20)))" ^
    -Dbbox=9,9,11,11 ^
    -Dgenerator=styled ^
    -Dui=true ^
    org.krahe.chris.mapgen.MapTool