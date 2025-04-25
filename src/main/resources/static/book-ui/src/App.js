import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { Layout } from 'antd';
import './App.css';

import LoginSelector from './components/LoginSelector';
import AuthorLogin from './components/AuthorLogin';
import UserView from './components/UserView';
import BookDetails from "./components/BookDetails";
import AuthorBooks from "./components/AuthorBooks";

const { Header, Content, Footer } = Layout;

function App() {
    return (
        <Router>
            <Layout className="layout" style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
                <Header className="header" style={{ display: 'flex', alignItems: 'center' }}>
                    <div className="logo">
                        <Link to="/" className="logo-link">Сайт с книгами</Link>
                    </div>
                    </Header>

                <Content className="site-content" style={{ flex: 1 }}>
                    <div className="content-wrapper">
                        <Routes>
                            <Route path="/" element={<LoginSelector />} />
                            <Route path="/author-login" element={<AuthorLogin />} />
                            <Route path="/author/:id/books" element={<AuthorBooks />} />
                            <Route path="/user" element={<UserView />} />
                            <Route path="/user/book/:id" element={<BookDetails />} />
                        </Routes>
                    </div>
                </Content>

                <Footer className="footer" style={{ textAlign: 'center' }}>
                    Created by X
                </Footer>
            </Layout>
        </Router>
    );
}

export default App;



