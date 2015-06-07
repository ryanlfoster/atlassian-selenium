package com.atlassian.webdriver.confluence.data;

/**
 * A class that represents a User
 */
public class User
{
    private final String username;
    private final String fullName;
    private final String email;
    private final String password;

    public User(String username, String fullName, String email)
    {
        this(username, null, fullName, email);
    }

    public User(String username, String password, String fullName, String email)
    {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;


    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFullName()
    {
        return fullName;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public boolean equals(final Object o)
    {

        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null)
        {
            return false;
        }
        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null)
        {
            return false;
        }
        if (username != null ? !username.equals(user.username) : user.username != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}