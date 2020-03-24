#!/usr/bin/env python3

import os
import re
import sys

RELEVANT_EXTENSIONS = ['data', 'services']
ADAPTATION_REGEX = re.compile('\s*//\s*A(?P<id>\d)')
MANDATORY_ADAPTATIONS = [2, 4, 5, 7]

def relevant_files(folder):
    if not os.path.isdir(folder):
        print('ERROR: "%s" is not an existing folder' % folder)
        sys.exit(4)

    foldersTodo = [folder]    

    for (folderPath, foldernames, filenames) in os.walk(folder):
        for filename in filenames:
            _, ext = os.path.splitext(filename)            
            if len(ext) < 2:
                continue

            if ext[1:] in RELEVANT_EXTENSIONS:
                yield os.sep.join([folderPath, filename])

def find_adapations(file):
    adaptationCounts = {}    
    with open(file,'r') as fd:
        for line in fd.readlines():
            match = ADAPTATION_REGEX.match(line)
            if not match:
                continue

            id = match.group('id')
            if id not in adaptationCounts:
                adaptationCounts[id] = 1
            else:
                adaptationCounts[id] += 1

    return adaptationCounts

def print_adaptation_counts(adaptationCounts, prefix='\t'):
    sum = 0
    mandatorySum = 0
    for id, count in sorted(adaptationCounts.items(), key=lambda i: i[0]):
        print('%sA%s: %s' % (prefix, id, count))
        sum += count
        if int(id) in MANDATORY_ADAPTATIONS:
            mandatorySum += count
    print('%s**SUM**: %d (mandatory: %d)' % (prefix, sum, mandatorySum))

if __name__ == '__main__':
    if len(sys.argv) < 2:
        sys.exit(4)

    folders = list(map(lambda f: os.path.abspath(f), sys.argv[1:]))
    relevantFiles = [
        file for folder in folders for file in relevant_files(folder)
    ]

    overall = {}
    perExtension = {}
    for file in relevantFiles:
        adaptationCounts = find_adapations(file)
        for id, count in adaptationCounts.items():
            if id not in overall:
                overall[id] = count
            else:
                overall[id] += count

            _, ext = os.path.splitext(file)
            if ext not in perExtension:
                perExtension[ext] = {id: count}
            elif id not in perExtension[ext]:
                perExtension[ext][id] = count
            else:
                perExtension[ext][id] += count

    print('Overall adaptation counts:')
    print_adaptation_counts(overall)

    print('\nCounts per extension:')
    sortedExtensions = sorted(perExtension.items(), key=lambda i: i[0])
    for extension, adaptationsCounts in sortedExtensions:
        print('\t%s:' % extension)
        print_adaptation_counts(adaptationsCounts, '\t\t')
