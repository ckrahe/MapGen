SET maplibCP=maplib\target\maplib-1.1.0.jar;maplib\target\dependency\*;
SET mapsimCP=mapsim\target\mapsim-1.1.0.jar;mapsim\target\dependency\*;
SET maptoolCP=maptool\target\maptool-1.1.0.jar;maptool\target\dependency\*
SET CP=%maplibCP%%mapsimCP%%maptoolCP%

java -cp "%CP%" ^
    -Dwkt="GEOMETRYCOLLECTION (POINT (10.2 10.2),POINT (10.3 10.3),POINT (10.4 10.4),POINT (10.5 10.5),POLYGON ((9.5 9.5, 10 9.5, 10 10, 9.5 10, 9.5 9.5)),LINESTRING (10.75 10.75, 10.8 10.7, 10.9 10.85))" ^
    -Dbbox=9,9,11,11 ^
    -Dgenerator=styled ^
    -Dui=false ^
    org.krahe.chris.mapgen.MapTool