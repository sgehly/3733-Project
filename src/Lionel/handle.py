from pycreate2 import Create2
import time
import threading
from gtts import gTTS
from playsound import playsound
import os


class instruction:
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
            print(test)
            for inst in getInstructions(test):
                executeInstruction(inst, bot)


class instruction:
    def __init__(self, type, value):
        self.type = type
        self.value = value


def getInstructions(packet):
    temp_packet = packet[1:len(packet)]
    instruction_list = []
    for i in temp_packet.split(','):
        if i[0] != '-':
            value = i[1:len(i)]
        else:
            value = 0
        type = i[0]
        instruction_list.append(instruction(type, int(value)))
    return instruction_list


def executeInstruction(inst, bot):
    if inst.type == "S":
        bot.drive_distance(1, 500)
    elif inst.type == "R":
        bot.turn_angle(inst.value, 100)
    elif inst.type == "L":
        bot.turn_angle(inst.value, -100)
    elif inst.type == "-":
        bot.drive_stop()
    time.sleep(1)


def intro_sequence(bot):
    thread.start()
    bot.drive_distance(1, 75)
    bot.turn_angle(360, 100)

