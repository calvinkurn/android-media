from io import StringIO
import xml.etree.ElementTree as ET
import re


def isSameLocation(dIssueLocation, pIssueLocation):
    dIssueLine = dIssueLocation.attrib.get("line")
    pIssueLine = pIssueLocation.attrib.get("line")

    dIssueColumn = dIssueLocation.attrib.get("column")
    pIssueColumn = pIssueLocation.attrib.get("column")

    dIssueFilePath = getFilePath(dIssueLocation.attrib.get("file"))
    pIssueFilePath = getFilePath(pIssueLocation.attrib.get("file"))

    if dIssueFilePath is None:
        return True

    return dIssueLine == pIssueLine and dIssueColumn == pIssueColumn and dIssueFilePath == pIssueFilePath


def getFilePath(workSpacePath):
    groups = re.findall("\/media\/tokopedia\/workspace\/[\w|-]*\/(.*)", workSpacePath)
    if len(groups) > 0:
        return groups[0]
    else:
        return None


def main():
    derivedLintTree = ET.parse(
        'customerapp/build/reports/lint-results-liveDevDebug.xml')
    baseLintTree = ET.parse(
        'customerapp/build/reports/base_branch-results-liveDevDebug.xml')
    for dIssue in derivedLintTree.getroot().findall("issue"):
        isToBeRemoved = False
        dIssueLocation = dIssue.find('location')
        for pIssue in baseLintTree.getroot().findall("issue"):
            pIssueLocation = pIssue.find('location')
            if isSameLocation(dIssueLocation, pIssueLocation):
                isToBeRemoved = True
                break
        if isToBeRemoved:
            derivedLintTree.getroot().remove(dIssue)

    derivedLintTree.write("customerapp/build/reports/test_result.xml")

if __name__ == "__main__":
    # calling main function
    main()