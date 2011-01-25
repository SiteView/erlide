package org.erlide.core;

import static org.junit.Assert.assertNotNull;

import org.erlide.core.erlang.IErlModule;
import org.erlide.core.erlang.IErlProject;
import org.erlide.test.support.ErlideTestUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void moduleWithNonErlangExtension() throws Exception {
        // given an erlang project
        final String projectName = "testproject";
        final IErlProject erlProject = ErlideTestUtils
                .createTmpErlProject(projectName);
        assertNotNull(erlProject);
        // when creating a module with non-erlang extension, e.g. erlx
        final IErlModule a = ErlideTestUtils
                .createModule(
                        erlProject,
                        "a.erlx",
                        "-module(a).\n-export([t/0]).\nt() ->\n    p(a).\np(L) ->\n    lists:reverse(L).\n");
        // then it should be created
        assertNotNull(a);
    }
}