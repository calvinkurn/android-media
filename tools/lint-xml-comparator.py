from io import StringIO
import xml.etree.ElementTree as ET


def isSameLocation(dIssueLocation, pIssueLocation):
    dIssueLine = dIssueLocation.attrib.get("line")
    dIssueColumn = dIssueLocation.attrib.get("column")
    dIssueFile = dIssueLocation.attrib.get("file")

    pIssueLine = pIssueLocation.attrib.get("line")
    pIssueColumn = pIssueLocation.attrib.get("column")
    pIssueFile = pIssueLocation.attrib.get("file")
    return dIssueLine == pIssueLine and dIssueColumn == pIssueColumn and dIssueFile == pIssueFile


def main():
    parentTree = ET.parse(
        'customerapp/build/reports/lint-results-liveDevDebug.xml')
    derivedTree = ET.parse(
        'customerapp/build/reports/lint_your_branch-results-liveDevDebug.xml')
    for dIssue in derivedTree.getroot().findall("issue"):
        isToBeRemoved = False
        dIssueLocation = dIssue.find('location')
        for pIssue in parentTree.getroot().findall("issue"):
            pIssueLocation = pIssue.find('location')
            if isSameLocation(dIssueLocation, pIssueLocation):
                isToBeRemoved = True
                break
        if isToBeRemoved:
            derivedTree.getroot().remove(dIssue)
    derivedTree.write("customerapp/build/reports/test_result.xml")
if __name__ == "__main__":
    # calling main function
    main()