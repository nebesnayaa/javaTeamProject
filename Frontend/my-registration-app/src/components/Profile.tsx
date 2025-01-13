import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Style/Profile.css";

const Profile: React.FC = () => {
  const [profileData, setProfileData] = useState({
    id: "",
    name: "",
    email: "",
    phone: "",
    gender: "",
    age: "",
    password: ""
  });

  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState(profileData);

  const [resumeData, setResumeData] = useState<null | { content: string }>({
    content: "",
  });
  const [resumeAvailable, setResumeAvailable] = useState(true);

  const [selectedTemplate, setSelectedTemplate] = useState<string>(""); // Состояние для выбранного шаблона
  const [templates] = useState([
    { id: "template1", name: "Template 1" },
    { id: "template2", name: "Template 2" },
    { id: "template3", name: "Template 3" },
  ]); // Пример массива шаблонов

  const navigate = useNavigate();

  useEffect(() => {
    const userId = "9cb97045-82ed-4c60-b60a-ef50c3499700"; // захардкорений userId
   
    console.log("Session ID from cookie:", userId);
    if (!userId) {
      console.error("User ID not found in cookies");
      navigate("/login"); // Якщо ID немає, перенаправляємо на логін
      return;
    }

    axios
      .get(`http://localhost:8080/users/one/${userId}`, { withCredentials: true })
      .then((response) => {
        setProfileData(response.data);
        setFormData(response.data);
      })
      .catch((error) => console.error("Error fetching profile data:", error));
    
      // axios
      // .get("/api/resumes/user", { withCredentials: true }) // поки що некоректний запит
      // .then((response) => {
      //   setResumeData(response.data);
      //   setResumeAvailable(true);
      // })
      // .catch((error) => {
      //   console.error("No resume found:", error);
      //   setResumeAvailable(false);
      // });
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSave = () => {
    axios
      .put(`http://localhost:8080/users`, formData, {
        withCredentials: true,
      })
      .then((response) => {
        setProfileData(response.data);
        setIsEditing(false);
      })
      .catch((error) => console.error("Error updating profile:", error));
  };

  const handleLogout = async () => {
    try {
      await axios.post("http://localhost:8080/users/logout", {}, { withCredentials: true });
      navigate("/login");
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };
  
  const handleCreateResume = () => {
    navigate("/resume", { state: { templateId: selectedTemplate } }); // Передаем выбранный шаблон
  };
  const handleTemplateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedTemplate(e.target.value);
  };

  return (
    <div>
      <div className="profilePanel">
        <nav className="navbar-profile">
          <a className="username">{profileData.name || "Username"}</a>
          <button className="btn-logout" onClick={handleLogout}>
            Logout
          </button>
        </nav>
      </div>
      <div className="wrapper">
        <div className="profile-container">
          <div className="navbar-profile">
            <h2>My profile</h2>
            {!isEditing ? (
              <a className="icon-edit" onClick={() => setIsEditing(true)}>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  height="24px"
                  viewBox="0 -960 960 960"
                  width="24px"
                  fill="#D9D9D9"
                >
                  <path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z" />
                </svg>
              </a>
            ) : (
              <></>
            )}
          </div>
          {!isEditing ? (
            <div className="infoFields">
              <p>Name: {profileData.name}</p>
              <p>Email: {profileData.email}</p>
              <p>Phone: {profileData.phone}</p>
              <p>Gender: {profileData.gender}</p>
              <p>Age: {profileData.age}</p>
            </div>
          ) : (
            <div className="infoFields">
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                placeholder="Name"
              />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                placeholder="Email"
              />
              <input
                type="text"
                name="phone"
                value={formData.phone}
                onChange={handleInputChange}
                placeholder="Phone"
              />
              <select
                id="gender"
                name="gender"
                value={formData.gender}
                onChange={(e) => handleInputChange}
                required
              >
                <option value="" disabled>Select your gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
              <input
                type="number"
                name="age"
                value={formData.age}
                onChange={handleInputChange}
                placeholder="Age"
              />
              <div className="buttons-container">
                <button className="btn-save" onClick={handleSave}>
                  Save
                </button>
                <button
                  className="btn-cancel"
                  onClick={() => setIsEditing(false)}
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
      <div className="resume-profile-container">
        <h3>My resume</h3>
        <label>Choose the template:</label>
        <select
          value={selectedTemplate}
          onChange={handleTemplateChange}
          className="template-select"
        >
          <option value="">Choose</option>
          {templates.map((template) => (
            <option key={template.id} value={template.id}>
              {template.name}
            </option>
          ))}
        </select>
        {resumeAvailable ? (
          <div>
            <p>{resumeData?.content}</p>
            <button>Edit resume</button>
          </div>
        ) : (
          <div className="create-resume">
            <p className="text">You haven't created any resume yet.</p>
            <button className="btn-add" onClick={handleCreateResume}>
              Create
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;
