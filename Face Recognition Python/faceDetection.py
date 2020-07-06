import face_recognition
from PIL import Image
import numpy as np
import cv2




"""
* Input --> image name in the form of string.
* Output --> No of faces, The Pixel Nparray of those faces.

* Process--> Convert the image into pixel array.
             Get the face locations.
             Get part of the original array using face locations.

"""
def getFacesFromFrame(pixel_array_of_image):

    #Detect all the faces
    # face_locations = face_recognition.face_locations(pixel_array_of_image)

    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
    grayscaleOfImage = cv2.cvtColor(pixel_array_of_image, cv2.COLOR_BGR2GRAY)
    face_locations = faces = face_cascade.detectMultiScale(grayscaleOfImage, 1.3, 5)

    #Use to know no of faces in each frame
    # print(len(face_locations))

    nparray_of_faces = []

    for i in range(len(face_locations)):
        lcn = face_locations[i]
        
        
        x1 = lcn[0]
        y1 = lcn[1]
        x2 = x1+ lcn[2]
        y2 = y1+ lcn[3]

        #Split the original img array to get only the pixel values where face is present.
        face = pixel_array_of_image[y1: y2+1, x1: x2+1]

        # Use to verify if the numpy array is divided correctly or not
        # iq = Image.fromarray(face, 'RGB')
        # iq.show()
        
        nparray_of_faces.append(face)

    return len(face_locations), nparray_of_faces






