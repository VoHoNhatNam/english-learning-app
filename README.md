# English Learning App

Android application for university students to learn English vocabulary.

## Architecture
MVVM + Repository

## Team
Leader: Vo Ho Nhat Nam

## Features
- Vocabulary learning
- Quiz system
- Firebase sync
- Ads integration
- Premium upgrade
## Git Workflow For Team

### Branch Structure

main → stable version (protected)
develop → development branch
feature-ui → UI development
feature-database → database / firebase
feature-logic → business logic

---

### How Members Work

1. Clone project

git clone https://github.com/VoHoNhatNam/english-learning-app.git

2. Go to project folder

cd english-learning-app

3. Checkout your branch

UI member:

git checkout feature-ui

Database member:

git checkout feature-database

4. Pull newest code before working

git pull

5. After coding

git add .
git commit -m "describe your change"
git push

---

### Create Pull Request

After pushing code:

feature branch → develop

Leader will review and merge.
