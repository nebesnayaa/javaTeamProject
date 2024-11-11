import axios from 'axios';

export const fetchAllUsers = async () => {
  try {
   const response = await axios.get('http://localhost:8888/users', {
      headers: {
          'Content-Type': 'application/json',
      }});
    console.log(response);
    return response.data;
  } catch (error) {
    console.error("Error fetching users:", error);
    throw error;
  }
};