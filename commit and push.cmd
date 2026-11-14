@echo off
setlocal

REM ============================================================
REM  git-commit-push.bat
REM  Stages all changes, commits with the given message, pushes.
REM
REM  Usage:
REM    git-commit-push.bat "Fix login bug"
REM    git-commit-push.bat            (prompts for the message)
REM ============================================================

REM --- Check Git ---------------------------------------------------
where git >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Git is not installed or not in PATH.
    exit /b 1
)

REM --- Check we are inside a repo -----------------------------------
git rev-parse --is-inside-work-tree >nul 2>&1
if errorlevel 1 (
    echo [ERROR] This directory is not a Git repository.
    exit /b 1
)

REM --- Get the commit message ---------------------------------------
set "MSG=%~1"
if "%MSG%"=="" set /p "MSG=Commit message: "
if "%MSG%"=="" (
    echo [ERROR] Commit message cannot be empty.
    exit /b 1
)

REM --- Determine current branch -------------------------------------
for /f "delims=" %%b in ('git rev-parse --abbrev-ref HEAD') do set "BRANCH=%%b"

REM --- 1) Stage everything ------------------------------------------
echo.
echo [1/3] Staging all changes...
git add -A
if errorlevel 1 (
    echo [ERROR] git add failed.
    exit /b 1
)

REM --- 2) Commit (only if there is something staged) ----------------
git diff --cached --quiet
if errorlevel 1 (
    echo [2/3] Committing...
    git commit -m "%MSG%"
    if errorlevel 1 (
        echo [ERROR] git commit failed.
        exit /b 1
    )
) else (
    echo [2/3] Nothing new to commit.
)

REM --- 3) Push -------------------------------------------------------
echo [3/3] Pushing branch "%BRANCH%"...
git rev-parse --abbrev-ref --symbolic-full-name @{u} >nul 2>&1
if errorlevel 1 (
    REM No upstream yet -> create it
    git push -u origin "%BRANCH%"
) else (
    git push
)
if errorlevel 1 (
    echo [ERROR] git push failed.
    exit /b 1
)

echo.
echo Done: "%MSG%" committed and pushed on "%BRANCH%".
endlocal
exit /b 0