import re
import os

df_cfg = "/buildconfig/appcompile/dynamic-feature-customerapp.cfg"
project_path = os.getcwd()

def updateDFGradle():
    list_of_modules = findDynamicFeaturesBuildGradleList()
    df_gradle_path = os.path.join(project_path, 'buildconfig/appcompile/enable-shrinkresource.gradle')
    with open (df_gradle_path, 'w') as f:
        f.write("dependencies {"+ '\n')
        for item in list_of_modules:
            #append item
            f.write(item+ '\n')

        f.write("}")
    return df_gradle_path


def findDynamicFeaturesBuildGradleList():
    df_cfg_full_path = project_path + df_cfg
    d = []
    with open(df_cfg_full_path) as f:
        contents = f.read()
        pattern = re.compile(r"v(.+)\:(\w.+)\:(\w.+)\:(\w+)?")
        result = re.finditer(pattern, contents)
        for r in result:
            #d.append("/" + r.group(2).strip() + "/" + r.group(3).strip() + "/" + r.group(4).strip())

            path = project_path + "/" + r.group(2).strip() + "/" + r.group(3).strip() + "/" + r.group(4).strip()
            d.extend(findProjectDependency(path))
    return d


def findProjectDependency(path):
    """
    Return list of object representing the path and name of project dependency that a module implements
    Input path is the path of module we want to find out
    """
    build_path = path + "/build.gradle"

    d = []
    with open(build_path) as f:
        contents = f.read()
        pattern = re.compile(
            r"implementation \(?project\(rootProject\.ext\.features\.(\w+)\)\)?")
        result = re.finditer(pattern, contents)
        for r in result:
            d.append( "implementation project(rootProject.ext.features." + r.group(1) + ")")
    return d

#print(os.getcwd())
print(updateDFGradle())
