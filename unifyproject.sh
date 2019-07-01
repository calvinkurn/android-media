#!/usr/bin/env bash

BRANCH_NAME='branchname'

help() {
    echo "start              => Start Development"
    echo "commit <message>   => Add + Commit to Unify"
    echo "push               => Push to Unify"
    echo "publish <project>  => Publish to Artifactory, list project: icon / principles / components"
}

startDev( ) {
    echo "Starting development..."
    #if [ `git branch | egrep "^[[:space:]]+${feature/unify/dev}$"` ];
    #then
    #    echo "git checkout -b feature/unify/dev"
    #else
    #    echo "git checkout feature/unify/dev"
    #fi
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
    BRANCH_NAME=$2
elif [ $1 = "commit" ]; then
    commit "$2"
elif [ $1 = "push" ]; then
    push
elif [ $1 = "publish" ]; then
     publish $2
else
    help
fi

