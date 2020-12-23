@echo off
ECHO SOURCE BRANCH IS %BUILD_SOURCEBRANCH%
ECHO GIT CHECKOUT MAIN
git checkout main
ECHO GIT STATUS
git status
ECHO GIT ADD
git add .
ECHO GIT STATUS
git status
ECHO GIT commit
git commit -m "CD commit"
ECHO GIT PUSH
git push origin
ECHO GIT STATUS
git status
