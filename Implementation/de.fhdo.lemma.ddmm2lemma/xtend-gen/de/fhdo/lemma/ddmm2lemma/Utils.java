package de.fhdo.lemma.ddmm2lemma;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class Utils {
  public static IFile getSelectedFile(final ExecutionEvent event) {
    try {
      final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
      ISelection _selection = window.getSelectionService().getSelection();
      final IStructuredSelection selection = ((IStructuredSelection) _selection);
      final Object selectedElement = selection.getFirstElement();
      return ((IFile) selectedElement);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static void serialize(final IFile file, final String filePath) {
    Utils.serialize(Utils.getXtextResource(file), file.getProject(), filePath);
  }
  
  public static void serialize(final Resource resource, final IProject project, final String filePath) {
    try {
      final IFile file = project.getFile(filePath);
      final URI uri = URI.createURI(file.getFullPath().toString());
      Resource _xifexpression = null;
      if ((resource instanceof XtextResource)) {
        Resource _xblockexpression = null;
        {
          final Resource newXmiResource = ((XtextResource)resource).getResourceSet().createResource(uri);
          boolean _isEmpty = ((XtextResource)resource).getContents().isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            newXmiResource.getContents().add(((XtextResource)resource).getContents().get(0));
          }
          _xblockexpression = newXmiResource;
        }
        _xifexpression = _xblockexpression;
      } else {
        Resource _xblockexpression_1 = null;
        {
          resource.setURI(uri);
          _xblockexpression_1 = resource;
        }
        _xifexpression = _xblockexpression_1;
      }
      final Resource xmiResource = _xifexpression;
      xmiResource.save(CollectionLiterals.<Object, Object>emptyMap());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static Resource getXtextResource(final IFile file) {
    try {
      final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
      final Resource resource = Utils.getXtextResourceSet(file).getResource(uri, true);
      resource.load(null);
      return resource;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static ResourceSet getXtextResourceSet(final IFile file) {
    final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(uri).<IResourceSetProvider>get(IResourceSetProvider.class).get(file.getProject());
  }
  
  public static String removeSuffix(final String s, final String suffix) {
    boolean _endsWith = s.endsWith(suffix);
    boolean _not = (!_endsWith);
    if (_not) {
      return s;
    }
    int _length = s.length();
    int _length_1 = suffix.length();
    int _minus = (_length - _length_1);
    return s.substring(0, _minus);
  }
  
  public static String removePrefix(final String s, final String prefix) {
    boolean _startsWith = s.startsWith(prefix);
    boolean _not = (!_startsWith);
    if (_not) {
      return s;
    }
    return s.substring(prefix.length());
  }
}
