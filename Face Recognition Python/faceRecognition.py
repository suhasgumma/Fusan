import face_recognition
import  numpy as np
import faceDetection as fd

def sameFace(pixel_array_of_face1,pixel_array_of_face2):
    face_encoding1 = np.array(face_recognition.face_encodings(pixel_array_of_face1))
    face_encoding2 = np.array(face_recognition.face_encodings(pixel_array_of_face2))

    return face_recognition.compare_faces(face_encoding1,face_encoding2)[0]

