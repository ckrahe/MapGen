SET maplibCP=maplib\target\maplib-1.1.0.jar;maplib\target\dependency\*;
SET mapsimCP=mapsim\target\mapsim-1.1.0.jar;mapsim\target\dependency\*;
SET maptoolCP=maptool\target\maptool-1.1.0.jar;maptool\target\dependency\*
SET CP=%maplibCP%%mapsimCP%%maptoolCP%

java -cp "%CP%" ^
    -Dwkt="MULTIPOINT (8.99 9.1,8.9999 9.2,9.0 9.3,9.1 9.4,10.7 10.3,10.8 10.2,10.9 10.1,10.99 10,10.9999 9.9,9.39 9.72)" ^
    -Dbbox=9,9,11,11 ^
    -Dgenerator=styled ^
    -Dui=true ^
    org.krahe.chris.mapgen.MapTool