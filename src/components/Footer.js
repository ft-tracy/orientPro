import "../styles/Footer.css";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-section contact">
          <h3>Contact Us</h3>
          <p>Email: info@orientpro.com</p>
          <p>Phone: +1 234 567 890</p>
          <p>Address: 123 Liberty St, City, Country</p>
        </div>
        <div className="footer-bottom">
          <p>&copy; 2024 OrientPro. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
