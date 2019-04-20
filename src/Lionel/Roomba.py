from pycreate2 import Create2
import time
import serial.tools.list_ports
import socket

bot = Create2("COM3")
bot.start()
bot.full()

class instruction:
    def __init__(self, type, value):
        self.type = type
        self.value = value


def getInstructions(packet):
    instruction_list = []
    for i in packet.split(','):
        if i[0] != '-':
            value = i[1:len(i)]
        else:
            value = 0
        type = i[0]
        instruction_list.append(instruction(type, int(value)))
    return instruction_list


def executeInstruction(inst):
    if inst.type == "S":
        bot.drive_distance(inst.value, 1000)
    elif inst.type == "R":
        bot.turn_angle(inst.value, 100)
    elif inst.type == "L":
        bot.turn_angle(inst.value, -100)
    elif inst.type == "-":
        bot.stop()


for inst in getInstructions("S1,R180,S1,-"):
    print(inst.type)
    print(inst.value)
    executeInstruction(inst)
