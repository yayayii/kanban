import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {Link, useParams, useNavigate} from 'react-router-dom';
import {useState, useEffect} from "react";

const TaskView = () => {
    const initialFormState = {
        name: '',
        description: '',
        status: ''
    };
    const [task, setTask] = useState(initialFormState);
    const id = useParams();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);

        fetch('/api/tasks?id=' + id.id, {
            method: 'GET',
            headers: {'Accept': 'application/json'},
        })
            .then(response => response.json())
            .then(data => {
                setTask(data);
                setLoading(false);
            });
    }, [id, setTask]);

    const remove = async (id) => {
        await fetch(`/api/tasks?id=${id}`, {
            method: 'DELETE'
        })
            .then(() => {
                navigate("/");
            });
    }

    if (loading) {
        return;
    }

    return (
        <div>
            <Container style={{marginTop: "3%"}}>
                <Form>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input disabled type="text" name="name" id="name" value={task.name}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input disabled type="textarea" name="description" id="description" value={task.description}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="status">Status</Label>
                        <Input disabled name="status" id="status" value={task.status}/>
                    </FormGroup>
                    <FormGroup>
                        <div align="center" style={{marginBottom: "10px"}}>
                            <ButtonGroup>
                                <Button size="sm" color="warning" tag={Link} to={"/edit/" + task.id}>Edit</Button>
                                <Button size="sm" color="danger" tag={Link} to={"/"}
                                        onClick={() => remove(task.id)}>Delete</Button>
                                <Button size="sm" color="secondary" tag={Link} to={"/"}>Back</Button>
                            </ButtonGroup>
                        </div>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    );
};

export default TaskView;
