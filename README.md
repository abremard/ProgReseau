# ProgReseau
<!-- vscode-markdown-toc -->
* 1. [Forewords](#Forewords)
* 2. [Version](#Version)
* 3. [Specifications](#Specifications)
* 4. [Compile and run](#Compileandrun)
	* 4.1. [Config](#Config)
	* 4.2. [Steps](#Steps)
		* 4.2.1. [For TCP chat system](#ForTCPchatsystem)
		* 4.2.2. [For UDP chat system](#ForUDPchatsystem)
		* 4.2.3. [For HTTP server](#ForHTTPserver)
* 5. [Important notes](#Importantnotes)
* 6. [Authors](#Authors)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name='Forewords'></a>Forewords
This repository contains 2 separate network communication systems projects : a socket-based TCP/UDP distributed chat system & a HTTP server for synchronous network communications.

##  2. <a name='Version'></a>Version
Stable release - version 1.0 - October 21, 2020

##  3. <a name='Specifications'></a>Specifications
Please refer to `/Objectives` directory

##  4. <a name='Compileandrun'></a>Compile and run

###  4.1. <a name='Config'></a>Config
- Visual Studio Code
- Language Support for Java extension
- Java SDK jdk1.8.0

###  4.2. <a name='Steps'></a>Steps
1) Compile using `javac -d classes src/stream/*.java` or `javac -d classes src/http/server/*.java` (depending on the project)

####  4.2.1. <a name='ForTCPchatsystem'></a>For TCP chat system
2) To run server `java -classpath classes stream.EchoServerMultiThreaded 8080` if port number is 8080
3) To connect a client `java -classpath classes stream.EchoClient localhost 8080` if connection host is localhost and port number is 8080

####  4.2.2. <a name='ForUDPchatsystem'></a>For UDP chat system
2) To connect a client to Multicast channel `java -classpath classes stream.MulticastClient`

####  4.2.3. <a name='ForHTTPserver'></a>For HTTP server
2) To run server `java -classpath classes http.server.WebServer`
3) You can now communicate with the server using your favorite browser for GET requests or a HTTP request service like Postman

##  5. <a name='Importantnotes'></a>Important notes
- server files are stored at `TP-HTTP-Code/files` so in the GET, POST, PUT... requests the URL would look like `http://localhost:8080/files/waves.mp4`
- client_files is just a utility folder for testing purposes. Add a client file inside it and try sending your file to the server using Postman's PUT request, your file will appear in `TP-HTTP-Code/files`

##  6. <a name='Authors'></a>Authors
- TOUT Iyad
- BREMARD Alexandre