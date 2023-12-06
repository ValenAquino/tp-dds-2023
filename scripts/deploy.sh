#!/bin/bash

# Variables
SERVER_USER="ubuntu"
SERVER_IP="129.146.122.134"
KEY_PATH="chainz.key"
JAR_FILE="../target/TPA-2023-1.0-SNAPSHOT-jar-with-dependencies.jar"
REMOTE_DIR="/home/ubuntu/tp"
REMOTE_SCRIPT_PATH="$REMOTE_DIR/setup_db.sh"
REMOTE_START_SCRIPT_PATH="$REMOTE_DIR/start.sh"

# Cambiar permisos de la private key
chmod 600 "$KEY_PATH"

# Subir el JAR usando SCP
scp -i "$KEY_PATH" "$JAR_FILE" "$SERVER_USER@$SERVER_IP:$REMOTE_DIR/"
if [ $? -ne 0 ]; then
    echo "Error al subir el archivo $JAR_FILE al directorio $REMOTE_DIR del server"
    exit 1
fi

# Correr el script setup_db.sh en el server
ssh -i "$KEY_PATH" "$SERVER_USER@$SERVER_IP" "bash $REMOTE_SCRIPT_PATH"
if [ $? -ne 0 ]; then
    echo "Error al ejecutar el script: $REMOTE_SCRIPT_PATH"
    exit 1
fi

# Terminar otros procesos ya corriendo
ssh -i "$KEY_PATH" "$SERVER_USER@$SERVER_IP" "pkill -f 'java -jar $REMOTE_DIR/$JAR_FILE'"
if [ $? -ne 0 ]; then
    echo "Error al finalizar procesos java"
fi

ssh -i "$KEY_PATH" "$SERVER_USER@$SERVER_IP" "nohup bash $REMOTE_START_SCRIPT_PATH > /dev/null 2>&1 & echo \$! > /tmp/start_script.pid"
if [ $? -ne 0 ]; then
    echo "Error al arrancar el proyecto $REMOTE_START_SCRIPT_PATH"
    exit 1
fi

echo "Deploy finalizado con éxito."
