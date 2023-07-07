import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label, Table} from 'reactstrap';
import {Link, useParams, useNavigate, useLocation} from 'react-router-dom';
import {useState, useEffect} from "react";
import React from 'react';

const TaskView = () => {
    const initialFormState = {
        name: '',
        description: '',
        status: '',
        subtasks: [],
        endTime: ''
    };
    const location = useLocation();
    const [task, setTask] = useState(initialFormState);
    let subtasks = [];
    const id = useParams();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);

        fetch('/api/' + (location.pathname.includes('epictasks') ? 'epictasks' : location.pathname.includes('subtasks') ? 'subtasks' : 'tasks') + '?id=' + id.id, {
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
        await fetch('/api/' + (location.pathname.includes('epictasks') ? 'epictasks' : location.pathname.includes('subtasks') ? 'subtasks' : 'tasks') + '?id=' + id, {
            method: 'DELETE'
        })
        .then(() => {
            navigate("/");
        });
    }

    const removeSubtask = async (subtask) => {
        await fetch('/api/subtasks?id=' + subtask.id, {
            method: 'DELETE'
        }).then(() => {
            window.location.reload();
        });
    }

    if (loading) {
        return <p>Loading...</p>;
    }

    if (task.subtasks != null) {
        subtasks = task.subtasks.map(subtask => {
            return <tbody>
                        <tr key={subtask.id}>
                            <td style={{overflow: "hidden", textOverflow: "ellipsis"}}>{subtask.name}</td>
                            <td>{subtask.status}</td>
                            <td>{subtask.endTime}</td>
                            <td>
                                <ButtonGroup>
                                    <Button size="sm" color="primary" tag={Link} to={'/view/subtasks/' + subtask.id}>View</Button>
                                    <Button size="sm" color="warning" tag={Link} to={'/edit/subtasks/' + subtask.id}>Edit</Button>
                                    <Button size="sm" color="danger" onClick={() => removeSubtask(subtask)}>Delete</Button>
                                </ButtonGroup>
                            </td>
                        </tr>
                    </tbody>
        });
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
                        <Label for="endTime">End time</Label>
                        <Input disabled name="endTime" id="endTime" value={task.endTime}/>
                    </FormGroup>
                    {task.subtasks != null ? <Container fluid>
                        <div style={{maxWidth: "75%", marginLeft: "auto", marginRight: "auto"}}>
                            <Table className="mt-4" style={{tableLayout: "fixed"}}>
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Status</th>
                                    <th>End time</th>
                                    <th width="10%">Actions</th>
                                </tr>
                                </thead>
                                {subtasks}
                            </Table>
                        </div>
                    </Container> : ''}
                    <FormGroup>
                        <div align="center" style={{marginBottom: "10px"}}>
                            <ButtonGroup>
                                <Button size="sm" color="warning" tag={Link} to={'/edit/' + (task.subtasks != null ? 'epictasks' : task.epictaskId != null ? 'subtasks' : 'tasks') + '/' + task.id}>Edit</Button>
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
