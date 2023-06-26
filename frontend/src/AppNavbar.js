import React, { useState } from 'react';
import {Collapse, Nav, Navbar, NavbarBrand, NavbarText, NavbarToggler, NavItem, NavLink} from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faGithub, faTelegram } from '@fortawesome/free-brands-svg-icons';

const AppNavbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <Navbar color="dark" dark expand="md">
            <NavbarBrand tag={Link} to="/">KANBAN</NavbarBrand>
            <NavbarToggler onClick={() => { setIsOpen(!isOpen) }}/>
            <Collapse isOpen={isOpen} navbar>
                <NavbarText style={{width: "100%"}}></NavbarText>
                <Nav className="justify-content-end" navbar>
                    <NavItem>
                        <NavLink href="https://t.me/comeDownToUs" >
                            <FontAwesomeIcon icon={faTelegram} style={{fontSize: "1.5rem"}}/>
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="https://github.com/yayayii/kanban">
                            <FontAwesomeIcon icon={faGithub} style={{fontSize: "1.5rem"}}/>
                        </NavLink>
                    </NavItem>
                </Nav>
            </Collapse>
        </Navbar>
    );
};

export default AppNavbar;