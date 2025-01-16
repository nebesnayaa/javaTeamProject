import axios from "axios";
import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

const PasswordRecoveryPage: React.FC = () => {
  const { token } = useParams<{ token: string }>();
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      console.log("Invalid or missing token.");
    }
  }, [token]);
    
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      axios
      .post("http://localhost:8080/users/password-token-validation", {token:token}, {
        withCredentials: true
      })
    .then (async (response) => {
      const data = response.data;
      console.log(data);
      if (response.status === 200) {
        alert("Password changed successfully. Please relogin");
        setTimeout(() => navigate("/login"), 3000); // Redirect to login after 3 seconds
      }
    })
    } catch (err) {
      console.error("Failed to update password.");
    }
  };

  return (
    <div className="pass-change-container">
      <h3>Change password</h3>
      <button className="btn-pass-change" onClick={handleSubmit}>Submit</button>
    </div>
  );
}

export default PasswordRecoveryPage;