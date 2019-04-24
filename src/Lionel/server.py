import socket
from math import pi

from playsound import playsound
from gtts import gTTS
from pycreate2 import Create2
import os
import time
import threading
from playsound import playsound
from gtts import gTTS
from pycreate2 import Create2
import os


HOST, PORT = '', 9999

input = ''
bot = Create2("COM3")
bot.start()
bot.safe()

count = 0


class Instruction:
    def __init__(self, type, value):
        self.type = type
        self.value = value


def lionking():
    playsound('lionking.mp3')


thread = threading.Thread(target=lionking)


def handle(x, bot):
    test = ''
    for eachOne in x:
        test = test + eachOne
        if eachOne == '-':
            print("packet: " + test)
            # intro_sequence(bot)
            for inst in get_instructions(test):
                execute_instruction(inst, bot)
                print(inst.type + " for " + str(inst.value))
            print("execution complete")


def get_instructions(packet):
    temp_packet = packet[1:len(packet)]
    instruction_list = []
    for i in temp_packet.split(','):
        if i[0] != '-':
            value = i[1:len(i)]
        else:
            value = 0
        type = i[0]
        instruction_list.append(Instruction(type, value))
    return instruction_list


def execute_instruction(inst, bot):
    if inst.type == "S":
        #straight(10)
        straight(int(inst.value))
    elif inst.type == "R":
        bot.turn_angle(int(inst.value), -100)
    elif inst.type == "L":
        bot.turn_angle(int(inst.value), 100)
    elif inst.type == "I":
        thread.start()
        bot.drive_distance(1, 100)
    elif inst.type == "M":
        global count
        count += 1
        notString = "notification" + str(count) + ".mp3"
        tts = gTTS(text=inst.value, lang='en')
        print notString
        tts.save(notString)
        playsound(notString)
        try:
            os.remove(notString)
        except OSError as e:
            print "Failed with: ", e.strerror
    elif inst.type == "-":
        bot.stop()
        time.sleep(1)
        bot.start()
        bot.safe()


def straight(distance):
    last_time = int(round(time.time() * 1000))
    kp = 5

    right_output = 324
    bot.drive_direct(300, 324)
    time.sleep(.1)
    genEnc = bot.get_sensors().encoder_counts_left
    last_enc_left = 00
    last_enc_right = 00
    while (bot.get_sensors().encoder_counts_left - genEnc)*(pi*72.0/508.8) < distance*10:
        curTime = (round(time.time() * 1000))
        enc_left = bot.get_sensors().encoder_counts_left
        enc_right = bot.get_sensors().encoder_counts_right
        vel_left = (enc_left - last_enc_left) / (curTime - last_time)
        vel_right = (enc_right - last_enc_right) / (curTime - last_time)
        last_time = curTime
        last_enc_left = enc_left
        last_enc_right = enc_right
        pid_output = (vel_right - vel_left) * kp
        right_output += pid_output
        bot.drive_direct(300, right_output)
    bot.drive_stop()


listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print('Serving HTTP on port %s ...' % PORT)
while True:
    client_connection, client_address = listen_socket.accept()
    request = client_connection.recv(1024)
    print("request: " + request)
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

