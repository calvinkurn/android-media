#!/usr/bin/env bash

help() {
    echo "start              => Start Development"
    echo "commit <message>   => Add + Commit to Unify"
    echo "push               => Push to Unify"
    echo "publish <project>  => Publish to Artifactory"
}

startDev( ) {
    if [ -z "$1" ]; then
        echo "Please input your username"
        exit 1
    fi

    if [ ! -f git ]; then
        echo "Please install git"
    fi

    echo "Starting development..."
    echo "Please wait..."

    echo "user_name=$1" > unifyproject.config
    source unifyproject.config
    branchname="feature/unify/$user_name"

    if [ `git branch | egrep "^[[:space:]]+${branchname}$"` ];
    then
        echo "Checkout branch $branchname..."
        git checkout $branchname
    else
        echo "Creating new branch..."
        git checkout -b $branchname feature/unify/dev
    fi

    echo "Finish!"
}

commit( ) {
    if [ -z "$1" ]; then
        echo "Please input commit message"
        exit 1
    fi

    git add .
    git commit -m "$1"
}

push( ) {
    source unifyproject.config
    branchname="feature/unify/$user_name"

    git push origin $branchname
}

publish( ) {

    if [ -z "$1" ]; then
        echo "Please specify project:"
        echo "icon"
        echo "principle"
        echo "component"
        echo "calendar"
        echo "coachmark"
        exit 1
    fi

    if [ $1 = "icon" ];
    then
        $projectname = "libraries/unify/unify_icon"
    elif [ $1 = "principle" ];
    then
        $projectname = "libraries/unify/unify_principles"
    elif [ $1 = "component" ];
    then
        $projectname = "libraries/unify/unify_components"
    elif [ $1 = "calendar" ];
    then
        $projectname = "libraries/unify/unify_compositions/calendar"
    elif [ $1 = "coachmark" ];
    then
        $projectname = "libraries/unify/unify_compositions/coach_mark"
    else
        echo "This $1 project not available yet. Please contact core-team for help"
        exit 1
    fi

    ./gradlew -p $projectname assembleRelease artifactoryPublish
}

if [ -z "$1" ]; then
    help
    exit 1
fi

if [ $1 = "start" ]; then
    startDev $2
elif [ $1 = "commit" ]; then
    commit "$2"
elif [ $1 = "push" ]; then
    push
elif [ $1 = "publish" ]; then
     publish $2
else
    help
fi
