# Animal Picture Frontend

## Overview

This module offers a user interface (UI) to generate and view animal pictures directly from your web browser by calling the `animal-picture-app`.

## Prerequisites

Before running the frontend, ensure that:
- The `animal-picture-app` is running and accessible.

## Configuration

To connect the frontend to the `animal-picture-app`, specify the REST URL of the application in the `js/app.js` file by modifying the `apiUrl` variable. For example:

```javascript
const apiUrl = 'http://localhost:8080/animal-picture';
```

This step is crucial for the frontend to communicate correctly with the backend service.

## Running the Frontend

To view the frontend:
- **Directly in Browser:** Simply open the `index.html` file in your browser.
- **Using a Web Server:** Serve the files using any standard web server.
