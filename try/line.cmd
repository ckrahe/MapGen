SET maplibCP=maplib\target\maplib-1.1.0.jar;maplib\target\dependency\*;
SET mapsimCP=mapsim\target\mapsim-1.1.0.jar;mapsim\target\dependency\*;
SET maptoolCP=maptool\target\maptool-1.1.0.jar;maptool\target\dependency\*
SET CP=%maplibCP%%mapsimCP%%maptoolCP%

java -cp "%CP%" ^
    -Dwkt="LINESTRING (10.75 10.75, 10.8 10.7, 10.9 10.85)" ^
    -Dbbox=9,9,11,11 ^
    -Dgenerator=styled ^
    -Dui=true ^
    org.krahe.chris.mapgen.MapTool