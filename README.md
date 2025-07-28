# Cricket Score App

## Features
- **Create Matches**: Users can create matches with 5-player teams and manage the game as it progresses.
- **Real-Time Score Updates**: Match scores are updated in real-time, with viewers able to follow the match as it happens.
- **Automatic Score Calculations**: The app automatically calculates runs, run rates, and switches player strikes at the end of each over.
- **Player and Bowler Management**: Batsmen are automatically selected according to the batting order, and the match handler selects the bowler at the end of each over.
- **Share Match State**: Users can share the current state of the match, including scores, player information, and match details.

## Technology
- **Firebase**: Utilized for real-time data synchronization, ensuring that all viewers see the latest match updates instantly.
- **Nav Graph**: Implemented to manage app navigation and transitions smoothly.
- **Single Activity Architecture**: The entire app is built on a single activity with multiple fragments, providing a streamlined and efficient structure.
- **Smooth Fragment Animations**: Introduced fluid animations between fragments for a polished user experience.

## Code Structure
The app follows a modular approach to ensure clarity and maintainability:
- **Activity**: Acts as the host for all fragments, managing the overall app structure.
- **Fragments**: Each fragment represents a different part of the app, such as match creation, score updates, and live match viewing.
- **Firebase Integration**: Handles real-time data updates and synchronization across all devices viewing the match.

## Screenshots

| Match Create | Home Creation | Real-Time Score Update |
|-------------|----------------|------------------------|
| ![1](https://github.com/user-attachments/assets/655778b0-40ea-4e6f-abba-ea746a031c29) | ![2](https://github.com/user-attachments/assets/733bb51d-0cac-4f77-a5c5-e79dde1a1904) | ![3](https://github.com/user-attachments/assets/1f91e43e-873a-495f-ae90-3d0d75bb39e1) |

| Strike Change | Bowler Selection | Viewr's Screen |
|-------------------|------------------|---------------|
| ![4](https://github.com/user-attachments/assets/b41baf64-0b6a-486c-9fed-1456f31686d2) | ![5](https://github.com/user-attachments/assets/909ee0ed-9e1f-4b14-996c-11c81014c3a6) | ![6](https://github.com/user-attachments/assets/fdfe8cd6-5ae2-47d9-8f34-7a79c7417140) |
