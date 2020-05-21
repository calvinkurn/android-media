from unidiff.patch import PatchSet
from StringIO import StringIO
import xml.etree.ElementTree as ET


def main():
    tree = ET.parse('customerapp/build/reports/lint-results-liveDevDebug.xml')
    root = tree.getroot()

    with open('customerapp/build/reports/gitDiff.txt', 'r') as file:
        data = file.read()

    patch_set = PatchSet(StringIO(data))
    # ElementList = []

    # change_list = []  # list of changes
    # [(file_name, [row_number_of_deleted_line],
    # [row_number_of_added_lines]), ... ]

    for child in root.findall("issue"):
        issueLocation = child.find('location')
        issuePath = issueLocation.attrib.get("file")
        issueLine = issueLocation.attrib.get("line")
        isToBeRemoved = True
        # 'features/category/kategori/src/main/java/com/tokopedia/kategori/adapters/CategoryLevelOneAdapter.kt'
        # "/media/tokopedia/workspace/Android_PR_Checker_Test/features/category/kategori/src/main/java/com/tokopedia/kategori/adapters/CategoryLevelTwoAdapter.kt"
        # if "CategoryLevelOneAdapter" in issuePath:
        #     print(issuePath)

        for patched_file in patch_set:
            file_path = patched_file.path  # file name
            del_line_no = [line.target_line_no
                           for hunk in patched_file for line in hunk
                           if line.is_added and
                           line.value.strip() != '']  # the row number of deleted lines
            if file_path in issuePath:
                if int(issueLine) in del_line_no:
                    isToBeRemoved = False
                    break
        if isToBeRemoved:
            tree.getroot().remove(child)
    tree.write("customerapp/build/reports/lint-results-liveDevDebug1.xml")

    # for patched_file in patch_set:
    #     file_path = patched_file.path  # file name
    #     # print('gitDiffFileName :' + file_path)
    #
    #     ElementList.append(fetchIssueForFile(root, file_path))
    #
    #     del_line_no = [line.target_line_no
    #                    for hunk in patched_file for line in hunk
    #                    if line.is_added and
    #                    line.value.strip() != '']  # the row number of deleted lines
    #
    #     #   print('deleted lines : ' + str(del_line_no))
    #
    #     # v1, v2 = checkIfIssueExist(file_path, del_line_no, root)
    #     # if v1:
    #     #     print(v2)
    #
    #     ad_line_no = [line.source_line_no for hunk in patched_file
    #                   for line in hunk if line.is_removed and
    #                   line.value.strip() != '']  # the row number of added liens
    #     # print('added lines : ' + str(ad_line_no))
    #     change_list.append((file_path, del_line_no, ad_line_no))


# def checkIfIssueExist(file_path, del_line_no, root):
#     for child in root.findall("issue"):
#         issueLocation = child.find("location")
#         issuePath = issueLocation.attrib.get("file")
#         if file_path in issuePath:
#             line = issueLocation.attrib.get("line")
#             if line in del_line_no:
#                 return True, issueLocation
#     return False, None


# def fetchIssueForFile(root, path):
#     elementList = []
#     for child in root.findall("issue"):
#         location = child.find('location')
#         issuePath = location.attrib.get("file")
#         if path in issuePath:
#             elementList.append(child)
#     return elementList


if __name__ == "__main__":
    # calling main function
    main()