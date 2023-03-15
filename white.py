import difflib
import filecmp
import os
from os.path import isfile, join

#from numpy import diff

PATH_TO_SERVER = "Server\\FinalProjectEkt_Server\\src\\"
PATH_TO_CLIENT = "Client\\finalProjectEkt_Client\\src\\"

lines=0
java_lines=0
fxml_lines=0
# count all lines of code in client project
for path, subdirs, files in os.walk(PATH_TO_CLIENT):
    for name in files:
        t = os.path.join(path, name)
        if t[-4:]== 'java'and "OCSF" not in t:
            java_lines += sum(1 for line in open(t,encoding='utf-8'))
        if t[-4:]== 'fxml' and "OCSF" not in t:
            fxml_lines += sum(1 for line in open(t,encoding='utf-8'))
            
# do the same in server project
for path, subdirs, files in os.walk(PATH_TO_SERVER):
    for name in files:
        t = os.path.join(path, name)
        if t[-4:]== 'java' and "OCSF" not in t:
            java_lines += sum(1 for line in open(t,encoding='utf-8'))
        if t[-4:]== 'fxml' and "OCSF" not in t:
            fxml_lines += sum(1 for line in open(t,encoding='utf-8'))


# add em up 
lines = fxml_lines + java_lines
print(f"{fxml_lines=}, {java_lines=}")
print("TOTAL LOC=",lines)
