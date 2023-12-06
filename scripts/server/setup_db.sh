#!/bin/bash
JAR_FILE="TPA-2023-1.0-SNAPSHOT-jar-with-dependencies.jar"
XML_FILE="persistence.xml"

if [ ! -f "$JAR_FILE" ]; then
    echo "JAR file not found: $JAR_FILE"
    exit 1
fi

if [ ! -f "$XML_FILE" ]; then
    echo "XML file not found: $XML_FILE"
    exit 1
fi

# Crear directorio temporal
mkdir -p temp/META-INF
cp "$XML_FILE" temp/META-INF/

# Actualizar el JAR con el contenido del directorio META-INF
jar uf "$JAR_FILE" -C temp/ META-INF

# Borrar directorio temporal
rm -rf temp

echo "Copied $XML_FILE into $JAR_FILE META-INF directory"
