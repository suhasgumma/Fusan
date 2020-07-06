#Use cv2 library developed by OpenCV
import cv2
import math

#Open the video file
vid = cv2.VideoCapture('AVPTeaser.mp4')

fps = vid.get(cv2.CAP_PROP_FPS)
# print(fps)
frameCount = 0

frameList = []

while(vid.isOpened()):
    ret, frame = vid.read()

    if ret == False:
        break
    

    #Append the center frame in the second
    if (frameCount%fps == math.floor(fps/2)):
        frameList.append(frame)
        # print(frame)
        
    #Use if u want to save the frame
    #cv2.imwrite(f'img{str(frameCount)}.jpg',frame)
    frameCount+=1

# print(frameCount/fps)
# print(len(frameList))


vid.release()
cv2.destroyAllWindows()

