import faceDetection
import faceRecognition
import cv2
import math
import face_recognition
import  numpy as np
import time
from PIL import Image


#QUICK SORT FOR SORTING FACE LISTS
def partition(arr,low,high): 
    i = ( low-1 )          
    pivot = len(arr[high] )    
  
    for j in range(low , high): 
  
        if   len(arr[j]) >= pivot: 
          
            i = i+1 
            arr[i],arr[j] = arr[j],arr[i] 
  
    arr[i+1],arr[high] = arr[high],arr[i+1] 
    return ( i+1 ) 
  
def quickSort(arr,low,high): 
    if low < high: 
  
        
        pi = partition(arr,low,high) 
  
        quickSort(arr, low, pi-1) 
        quickSort(arr, pi+1, high) 



def sameFace(pixel_array_of_face1,pixel_array_of_face2):
    face_encoding1 = np.array(face_recognition.face_encodings(pixel_array_of_face1, known_face_locations=[(0, np.shape(pixel_array_of_face1)[1], np.shape(pixel_array_of_face1)[0], 0)]))
    face_encoding2 = np.array(face_recognition.face_encodings(pixel_array_of_face2, known_face_locations=[(0, np.shape(pixel_array_of_face2)[1], np.shape(pixel_array_of_face2)[0], 0)]))

    return face_recognition.compare_faces(face_encoding1,face_encoding2)[0]



def analyzeVideo(fileName):

    #Capture The video
    vid = cv2.VideoCapture(fileName)

    #Get "Frames Per Second" Of the video
    fps = vid.get(cv2.CAP_PROP_FPS)
    
    #Counting No of Frames
    frameCount = 0
    

    #The frame List..
    #Representation is pixel array
    frameList = []

    
    while(vid.isOpened()):
        ret, frame = vid.read()

        if ret == False:
            break

        #Append the center frame of the second....
        if (math.floor(frameCount%fps) == math.floor(fps/2)):
            frameList.append(frame)
            
        # frameList.append(frame)
            
        #Use if u want to save the frame
        #cv2.imwrite(f'img{str(frameCount)}.jpg',frame)

        frameCount+=1

    #By the end of this we have fps, frameList

    
    vid.release()
    cv2.destroyAllWindows()
    


    #By the end of this we have fps, frameList

    final_face_list = []

    total_faces = 0


    for frame in frameList:
        
        number_of_faces, face_list = faceDetection.getFacesFromFrame(frame)
        
        total_faces+= number_of_faces

        for face in face_list:
            found = False
            for foundface in final_face_list:
                if sameFace(foundface[0], face):
                    foundface.append(face) 
                    found = True
                    break
            if not found:
                final_face_list.append([face])

    quickSort(final_face_list, 0, len(final_face_list)-1)

    timePeriods = []

    for i in range(0, len(final_face_list)):
        face = final_face_list[i]
        timePeriods.append(len(face))

        image = Image.fromarray(face[0])
        image.save(str(i)+ ".png")

    return timePeriods






