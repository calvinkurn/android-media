#!/usr/bin/env bash

help() {
    echo "start              => Start Development"
    echo "commit <message>   => Add + Commit to Unify"
    echo "push               => Push to Unify"
    echo "publish <project>  => Publish to Artifactory, list project: icon / principles / components"
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
    echo "Commit !"
    echo $BRANCH_NAME
}

push( ) {
    echo "Push !"
}

publish( ) {
    echo "Publish !"
    echo "$1"
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
