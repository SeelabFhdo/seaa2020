package de.fhdo.lemma.ddmm2lemma;

import de.fhdo.lemma.ddmm2lemma.Utils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class XmiTransformationHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    final IFile selectedFile = Utils.getSelectedFile(event);
    StringConcatenation _builder = new StringConcatenation();
    String _name = selectedFile.getName();
    _builder.append(_name);
    _builder.append(".xmi");
    Utils.serialize(selectedFile, _builder.toString());
    return null;
  }
}
