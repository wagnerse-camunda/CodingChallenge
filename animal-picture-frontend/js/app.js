document.getElementById('generateCat').addEventListener('click', function() { generateAnimal('cat'); });
document.getElementById('generateBear').addEventListener('click', function() { generateAnimal('bear'); });
document.getElementById('generateDog').addEventListener('click', function() { generateAnimal('dog'); });

async function generateAnimal(animalType) {
  try {
    const apiUrl = "http://localhost:8080/animal-picture";

    let response = await fetch(`${apiUrl}/${animalType}`, {
      method: 'POST'
    });

    if (response.status !== 201) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const imageUrl = `${apiUrl}/${animalType}`;

    const imgElement = document.getElementById('animalImage');
    imgElement.src = imageUrl;
    imgElement.style.display = 'block';
  } catch (error) {
    console.error('Error generating animal picture:', error);
    displayErrorMessage(error.toString());
  }
}

function displayErrorMessage(message) {
  const existingError = document.getElementById('error-message');
  if (existingError) {
    existingError.remove();
  }

  const errorMessage = document.createElement('p');
  errorMessage.id = 'error-message';
  errorMessage.style.color = 'red';
  errorMessage.textContent = message;

  document.body.appendChild(errorMessage);
}
