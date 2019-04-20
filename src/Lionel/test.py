from gtts import gTTS
from playsound import playsound
import os

tts = gTTS(text='bonjour', lang='fr')
tts.save("good.mp3")
os.system("mpg321 good.mp3")
playsound("good.mp3")
