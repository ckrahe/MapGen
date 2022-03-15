SET maplibCP=maplib\target\maplib-1.1.0.jar;maplib\target\dependency\*;
SET mapsimCP=mapsim\target\mapsim-1.1.0.jar;mapsim\target\dependency\*;
SET maptoolCP=maptool\target\maptool-1.1.0.jar;maptool\target\dependency\*
SET CP=%maplibCP%%mapsimCP%%maptoolCP%

java -cp "%CP%" ^
    -Dwkt="POINT (10 10)" ^
    -Dbbox=9,9,11,11 ^
    -Dgenerator=styled ^
    -Dui=true ^
    org.krahe.chris.mapgen.MapTool