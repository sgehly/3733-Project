import socket
from pycreate2 import Create2
from handle import handle
import playsound
import threading

HOST, PORT = '', 9999

input = ''
bot = Create2("COM3")
bot.start()
bot.full()

listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print('Serving HTTP on port %s ...' % PORT)
while True:
    client_connection, client_address = listen_socket.accept()
    request = client_connection.recv(1024)
    print(request)
    input = input + request
    http_response = """\
HTTP/1.1 200 OK

This is a test
"""
    handle(input, bot)
    if request == '-':
        input = ''
    client_connection.sendall(http_response)
    client_connection.close()
