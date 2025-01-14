import { useNavigate, useLocation} from "react-router-dom";
import "./ResumeTemplate1.css";
import html2pdf from 'html2pdf.js';


const ResumeTemplate1: React.FC= () => {
  const location = useLocation();
  const navigate = useNavigate(); 

  const { data } = location.state || {}; // Отримуємо передані дані
  
  if (!data) {
    return <p>Loading...</p>; // Можна відобразити повідомлення, якщо дані ще не завантажені
  }
  
  const handleBack = () => {
    navigate(-1);
  };

  const handleDownloadPDF = () => {
    const element = document.getElementById("resume-to-pdf");
    if (element) {
      const opt = {
        margin: 0.2,
        filename: 'resume.pdf',
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
      };
      html2pdf().from(element).set(opt).save();
    }
  };

  return (
    <div className="resume-container1">
      {/* Контейнер для резюме */}
      <div className="resume-content-container" id="resume-to-pdf">
        <div className="upper-line1"></div>
        <div className="resume-header1">
          <h1 className="resume-name1">{data.fullName}</h1>
          <p className="resume-age-gender1">
            Age: {data.user.age}, 
            Gender: {data.user.gender === "male" ? "Male" : data.user.gender === "female" ? "Female" : "Other"}
          </p>
        </div>
        <div className="resume-content1">
          <div className="resume-section1">
            <h3>Contacts</h3>
            <p>Email: {data.user.email} Phone: {data.user.phone}</p>
          </div>

          <div className="resume-section1">
            <h3>Position</h3>
            <p>{data.position}</p>
          </div>

          <div className="resume-section1">
            <h3>Aim</h3>
            <p>{data.objective}</p>
          </div>

          <div className="resume-section1">
            <h3>Education</h3>
            <p>{data.education}</p>
          </div>

          <div className="resume-section1">
            <h3>Work experience</h3>
            <p>{data.workExperience}</p>
          </div>

          <div className="resume-section1">
            <h3>Skills and awards</h3>
            <p>{data.skillsAndAwards}</p>
          </div>

          <div className="resume-section1">
            <h3>Languages</h3>
            <p>{data.languages}</p>
          </div>
        </div>
      </div>

      {/* Контейнер для кнопок внизу */}
      <div className="buttons-container">
        <div className="resume-back-button">
          <button onClick={handleBack} className="btn-back">
            Back
          </button>
        </div>

        <div className="resume-download-button">
          <button onClick={handleDownloadPDF} className="btn-download">
            Download as PDF
          </button>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate1;