import React from "react";
import { withRouter } from "react-router-dom";
import { compose } from "redux";
import { fade, withStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import Typography from "@material-ui/core/Typography";
import InputBase from "@material-ui/core/InputBase";
import MenuItem from "@material-ui/core/MenuItem";
import Menu from "@material-ui/core/Menu";
import SearchIcon from "@material-ui/icons/Search";
import AccountCircle from "@material-ui/icons/AccountCircle";
import MoreIcon from "@material-ui/icons/MoreVert";
import { Link } from "react-router-dom";

const styles = theme => {
  return {
    grow: {
      flexGrow: 1
    },
    menuButton: {
      marginRight: theme.spacing(2)
    },
    title: {
      display: "none",
      [theme.breakpoints.up("sm")]: {
        display: "block"
      }
    },
    search: {
      position: "relative",
      borderRadius: theme.shape.borderRadius,
      backgroundColor: fade(theme.palette.common.white, 0.15),
      "&:hover": {
        backgroundColor: fade(theme.palette.common.white, 0.25)
      },
      marginRight: theme.spacing(2),
      marginLeft: 0,
      width: "100%",
      [theme.breakpoints.up("sm")]: {
        marginLeft: theme.spacing(3),
        width: "auto"
      }
    },
    searchIcon: {
      width: theme.spacing(7),
      height: "100%",
      position: "absolute",
      pointerEvents: "none",
      display: "flex",
      alignItems: "center",
      justifyContent: "center"
    },
    inputRoot: {
      color: "inherit"
    },
    inputInput: {
      padding: theme.spacing(1, 1, 1, 7),
      transition: theme.transitions.create("width"),
      width: "100%",
      [theme.breakpoints.up("md")]: {
        width: 200
      }
    },
    sectionDesktop: {
      display: "none",
      [theme.breakpoints.up("md")]: {
        display: "flex"
      }
    },
    sectionMobile: {
      display: "flex",
      [theme.breakpoints.up("md")]: {
        display: "none"
      }
    }
  };
};
const menuId = "primary-search-account-menu";
const mobileMenuId = "primary-search-account-menu-mobile";
class Header extends React.Component {
  state = {
    anchorEl: null,
    mobileMoreAnchorEl: null
  };

  setAnchorEl(val) {
    this.setState({ anchorEl: val });
  }

  setMobileMoreAnchorEl(val) {
    this.setState({ mobileMoreAnchorEl: val });
  }

  handleProfileMenuOpen = event => {
    this.setAnchorEl(event.currentTarget);
  };

  handleMobileMenuClose = () => {
    this.setMobileMoreAnchorEl(null);
  };

  handleMenuClose = () => {
    this.setAnchorEl(null);
    this.handleMobileMenuClose();
  };

  handleMobileMenuOpen = event => {
    this.setMobileMoreAnchorEl(event.currentTarget);
  };

  redirectToNewBlog = () => {
    this.setAnchorEl(null);
    this.handleMobileMenuClose();
    this.props.history.push("/blog/new?edit=true");
  };

  redirectToSignOut = () => {
    this.setAnchorEl(null);
    this.handleMobileMenuClose();
    this.props.history.push("/user/signout");
  };

  handleSignIn = () => {
    this.setAnchorEl(null);
    this.handleMobileMenuClose();
    this.props.history.push("/user/signin");
  };

  renderMenu = () => {
    return (
      <Menu
        anchorEl={this.state.anchorEl}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        id={menuId}
        keepMounted
        transformOrigin={{ vertical: "top", horizontal: "right" }}
        open={Boolean(this.state.anchorEl)}
        onClose={this.handleMenuClose}
      >
        <MenuItem onClick={this.redirectToSignOut}>Sign Out</MenuItem>
        <MenuItem onClick={this.handleMenuClose}>Profile</MenuItem>
        <MenuItem onClick={this.handleMenuClose}>My account</MenuItem>
        <MenuItem onClick={this.redirectToNewBlog}>Add a new Blog</MenuItem>
      </Menu>
    );
  };

  renderMobileMenu = () => {
    return (
      <Menu
        anchorEl={this.state.mobileMoreAnchorEl}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        id={mobileMenuId}
        keepMounted
        transformOrigin={{ vertical: "top", horizontal: "right" }}
        open={Boolean(this.state.mobileMoreAnchorEl)}
        onClose={this.handleMobileMenuClose}
      >
        <MenuItem onClick={this.handleProfileMenuOpen}>
          <IconButton
            aria-label="account of current user"
            aria-controls="primary-search-account-menu"
            aria-haspopup="true"
            color="inherit"
          >
            <AccountCircle />
          </IconButton>
          <p>Profile</p>
        </MenuItem>
      </Menu>
    );
  };
  renderAuthenticated = () => {
    return (
      <>
        {this.renderMobileMenu()}
        {this.renderMenu()}
      </>
    );
  };
  renderLoginRedirect = () => {
    return (
      <Button onClick={this.handleMenuClose} variant="outlined">
        Sign In
      </Button>
    );
  };
  render() {
    const { classes } = this.props;
    debugger;
    return (
      <div>
        <AppBar position="static">
          <Toolbar>
            <Typography className={classes.title} variant="h6" noWrap>
              YouBlog IT
            </Typography>
            <div className={classes.search}>
              <div className={classes.searchIcon}>
                <SearchIcon />
              </div>
              <InputBase
                placeholder="Search…"
                classes={{
                  root: classes.inputRoot,
                  input: classes.inputInput
                }}
                inputProps={{ "aria-label": "search" }}
              />
            </div>
            <div className={classes.grow} />
            {this.props.authenticated ? (
              <>
                <div className={classes.sectionDesktop}>
                  <IconButton
                    edge="end"
                    aria-label="account of current user"
                    aria-controls={menuId}
                    aria-haspopup="true"
                    onClick={this.handleProfileMenuOpen}
                    color="inherit"
                  >
                    <AccountCircle />
                  </IconButton>
                </div>
                <div className={classes.sectionMobile}>
                  <IconButton
                    aria-label="show more"
                    aria-controls={mobileMenuId}
                    aria-haspopup="true"
                    onClick={this.handleMobileMenuOpen}
                    color="inherit"
                  >
                    <MoreIcon />
                  </IconButton>
                </div>
                {this.renderAuthenticated()}
              </>
            ) : (
              <Button onClick={this.handleSignIn} variant="contained">
                Sign In
              </Button>
            )}
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}
Header = withRouter(Header);
export default withStyles(styles)(Header);
