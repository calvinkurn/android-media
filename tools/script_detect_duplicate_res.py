#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu May 14 09:20:56 2020

@author: sandeepgupta-xps
"""

import hashlib, os
from collections import defaultdict
import argparse
from fnmatch import fnmatch

# This function find all the images in the directory passed as a parameter
def findDuplicateImages(directory_path, image_to_search_list):
    # Patterns of file to compare
    patternPng = "*.png"
    patternJpg = "*.jpg"
    patternWebp = "*.webp"
    patternXml = "*.xml"
    complete_list = defaultdict(list)
    name_to_val = dict()

    for path, subdirs, files in os.walk(directory_path):
        for filename in files:
            if path.find("/build/") == -1 and (
                    fnmatch(filename, patternPng) or
                    fnmatch(filename, patternJpg) or
                    fnmatch(filename, patternWebp) or
                    fnmatch(filename, patternXml)):
                fln=path+"/"+filename
                if os.path.isfile(fln):
                    with open(fln, 'rb') as f:
                        file_val = hashlib.md5(f.read()).hexdigest()
                        name_to_val[fln] = file_val
                    if file_val not in complete_list:
                        complete_list[file_val]=[fln]
                    else:
                        complete_list[file_val].append(fln)

    # Validate the list passed
    for sf in image_to_search_list:
        sf = directory_path + "/" + sf
        if sf in name_to_val :
            find_file_entry = name_to_val[sf]
            if len(complete_list[find_file_entry]) > 1 :
                print("similar image/s found for file : " + sf)
                for file_item in complete_list[find_file_entry]:
                    if file_item != sf :
                        print(file_item)
                print("\n")
            else :
                print("no duplicate found for file : "+sf)
                print("\n")
        else:
            print("no entry found for file : "+sf)
            print("\n")

################################################
############# Argument Definition ##############
################################################

ap = argparse.ArgumentParser()
ap.add_argument("-p", "--path", required=True,
                help="set of all images to search through i.e. available drawables")
ap.add_argument('-l', '--list', required=True,
                help="set of images we are searching for i.e. newly added file location")

args = vars(ap.parse_args())
all_images_path = args["path"]
args = ap.parse_args()

if args.list is None:
    print("List to check is not passed")
else:
    image_to_search_list = [item for item in args.list.split(',')]


# Call this function to find duplicates images in directory
findDuplicateImages(all_images_path, image_to_search_list)
