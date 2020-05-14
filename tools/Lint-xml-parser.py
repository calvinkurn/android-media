import xml.etree.ElementTree as ET


tree = ET.parse('../customerapp/build/reports/lint-results-liveDevDebug.xml')
root = tree.getroot()
for child in root.findall("issue"):
	location = child.find('location')
        path = location.attrib.get("file")
        if "/features/category/" in path:
            print(ET.tostring(child, encoding='utf8', method='xml'), "\n")
        else:
            tree.getroot().remove(child)
tree.write("../customerapp/build/reports/lint-results-liveDevDebug1.xml")

