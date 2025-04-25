import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { Card, Row, Col, Typography, Spin, Button, Collapse } from "antd";

const { Title, Paragraph } = Typography;
const { Panel } = Collapse;

const BookDetails = () => {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/books/${id}`)
            .then((res) => res.json())
            .then((data) => {
                setBook(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Ошибка при загрузке книги:", err);
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-[80vh]">
                <Spin size="large" />
            </div>
        );
    }
    if (!book) {
        return (
            <div className="text-center mt-20">
                <Title level={3}>Книга не найдена.</Title>
            </div>
        );
    }

    const authors = book.authors?.map((a) => a.name).join(", ");

    return (
        <Row justify="center" className="p-6">
            <Col xs={24} sm={20} md={16} lg={12}>
                <Card
                    hoverable
                    cover={
                        <img
                            alt={book.title}
                            src={book.imagePath ? `/${book.imagePath}` : "/placeholder-image.jpg"}
                            style={{
                                width: "100%",
                                height: "400px",
                                objectFit: "contain", // вписывает изображение внутрь, не обрезая
                                backgroundColor: "#f0f0f0", // можно добавить фон
                            }}
                        />

                    }
                >
                    <Title level={2} className="whitespace-normal break-words">
                        {book.title}
                    </Title>
                    <Paragraph className="whitespace-normal break-words">
                        <b>Авторы:</b> {authors}
                    </Paragraph>

                    <Collapse accordion className="mt-6">
                        {[...book.chapters]
                            .sort((a, b) => a.id - b.id)
                            .map((ch) => (
                                <Panel header={ch.title} key={ch.id}>
                                    <Paragraph className="whitespace-pre-line break-words">
                                        {ch.content}
                                    </Paragraph>
                                </Panel>
                            ))}
                    </Collapse>

                    <div className="text-center mt-6">
                        <Link to="/user">
                            <Button type="primary">Назад к списку</Button>
                        </Link>
                    </div>
                </Card>
            </Col>
        </Row>
    );
};

export default BookDetails;


