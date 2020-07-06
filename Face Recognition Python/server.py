from socket import *
import json
import numpy as np
import finalWork
import base64

def write_binary(binary_string):
    videoFile = open('ffer.mp4', 'wb')

    videoFile.write(binary_string)
    videoFile.close()


HOST = "192.168.43.179"
PORT = 7000 

s = socket(AF_INET, SOCK_STREAM)
s.bind((HOST, PORT))

s.listen(1)

print("Listening...........")

conn, addr = s.accept()

print( "Connected by: " , addr)

full_message = b''

while True:
    msg = conn.recv(8196)
    if len(msg) <= 0:
        break

    full_message+= msg


print("Video Recieved.")


#create file
write_binary(full_message)

timePeriods = finalWork.analyzeVideo('ffer.mp4')
noOfFaces = len(timePeriods)

print(timePeriods, noOfFaces)

conn.close()

s.listen(1)

print("Listening Again.....")

conn, addr = s.accept()
print("Connection Established...")

messageToBeSent = str(noOfFaces).encode("UTF-8")
conn.send(len(messageToBeSent).to_bytes(2, byteorder='big'))
conn.send(messageToBeSent)

timePeriodSent = False

for i in range(noOfFaces):
    fileName = str(i)+ '.png'

    messageToBeSent = str(timePeriods[i]).encode("UTF-8")
    conn.send(len(messageToBeSent).to_bytes(2, byteorder='big'))
    conn.send(messageToBeSent)
    timePeriodSent = True

    with open(fileName,"rb") as imageFile:
        base64String = base64.encodebytes(imageFile.read())

    base64realString = base64String.decode('utf-8')

    base64realString = base64realString.encode("UTF-8")
    conn.send(len(base64realString).to_bytes(2, byteorder='big'))
    conn.send(base64realString)

    


conn.close()