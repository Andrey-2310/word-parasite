import speech_recognition as sr
import file_manager as fm

FILE_NAME = '/home/andrey/Documents/Projects/WordParasite/logs/parsed_speech.txt'

# obtain audio from the microphone
r = sr.Recognizer()

with sr.Microphone() as source:
    r.adjust_for_ambient_noise(source)
    while True:
        print("Say something!")
        audio = r.listen(source)

        # recognize speech using Google Speech Recognition
        try:
            fm.write_to_file(FILE_NAME, r.recognize_google(audio, language='ru') + '\n')
        except sr.UnknownValueError:
            print("Google Speech Recognition could not understand audio")
        except sr.RequestError as e:
            print("Could not request results from Google Speech Recognition service; {0}".format(e))
