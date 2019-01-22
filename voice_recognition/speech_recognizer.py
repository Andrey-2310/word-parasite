import speech_recognition as sr
import file_manager as fm

FILE_NAME = '/home/ITRANSITION.CORP/a.redkovsky/Documents/Projects/VoiceRecognition/logs/parsed_speech.txt'

# obtain audio from the microphone
r = sr.Recognizer()

with sr.Microphone() as source:
    while True:
        r.adjust_for_ambient_noise(source)
        print("Say something!")
        audio = r.listen(source, phrase_time_limit=5)

        # recognize speech using Google Speech Recognition
        try:
            parsed_text = r.recognize_google(audio, language='ru')
            print(parsed_text)
            fm.write_to_file(FILE_NAME, parsed_text + '\n')
        except sr.UnknownValueError:
            print("Google Speech Recognition could not understand audio")
        except sr.RequestError as e:
            print("Could not request results from Google Speech Recognition service; {0}".format(e))
