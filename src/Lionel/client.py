import socket
import sys

host = '130.215.120.185'
port = 9999

try:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
except socket.error:
    print('Failed to create socket')
    sys.exit()
try:
    remote_ip = socket.gethostbyname( host )
except socket.gaierror:
    print('Hostname could not be resolved. Exiting')
    sys.exit()
s.connect((remote_ip , port))
request = "This is a test"

try:
    s.sendall(request)
except socket.error:
    print('Send failed')
    sys.exit()

print('# Receive data from server')
reply = s.recv(4096)

print(reply)